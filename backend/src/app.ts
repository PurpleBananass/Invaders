import express, { Application } from 'express';
import morgan from 'morgan';
import { Server } from 'http';
import { useContainer, useExpressServer } from 'routing-controllers';
import Container from 'typedi';
import { join } from 'path';
import { createConnection, useContainer as useDBContainer } from 'typeorm';
import bodyParser from 'body-parser';
import { GlobalErrorHandler } from 'middleware/global-error.handler';
import { AuthorizationHandler } from 'middleware/authorization.handler';
import { IS_DEV } from 'config';
import cors from 'cors';
import { MysqlConnectionOptions } from 'typeorm/driver/mysql/MysqlConnectionOptions';

export class App {
  public app: Application;
  public server: Server;
  public port: number;

  constructor(port: number) {
    this.app = express();
    this.port = port;
    this.setMiddleWare();
  }

  public async initServer() {
    await new Promise((resolve) => {
      this.createDatabaseConnection().then(() => {
        useContainer(Container);

        useExpressServer(this.app, {
          controllers: [join(__dirname + `/api/**/*.controller.${IS_DEV ? 'ts' : 'js'}`)],
          middlewares: [GlobalErrorHandler],
          authorizationChecker: AuthorizationHandler,
          defaultErrorHandler: false,
        });

        this.server = this.app.listen(this.port, () => {
          console.log('Service Start');
          resolve(true);
        });
      });
    });
  }

  private setMiddleWare() {
    this.app.use(morgan('dev'));
    this.app.use(bodyParser.json());
    this.app.use(bodyParser.urlencoded({ extended: true }));
    this.app.use(cors());
  }

  private async createDatabaseConnection() {
    const connectionOpts: MysqlConnectionOptions = {
      type: 'mysql',
      entities: [join(__dirname + `/api/**/*.entity.${IS_DEV ? 'ts' : 'js'}`)],
      synchronize: true,
      host: process.env.DB_HOST || 'localhost',
      port: 3306,
      database: process.env.DB_NAME || 'test',
      username: process.env.DB_USER || 'root',
      password: process.env.DB_PASSWORD || 'root',
    };

    useDBContainer(Container);
    await createConnection(connectionOpts);
  }
}
