"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.App = void 0;
const express_1 = __importDefault(require("express"));
const morgan_1 = __importDefault(require("morgan"));
const routing_controllers_1 = require("routing-controllers");
const typedi_1 = __importDefault(require("typedi"));
const path_1 = require("path");
const typeorm_1 = require("typeorm");
const body_parser_1 = __importDefault(require("body-parser"));
const global_error_handler_1 = require("middleware/global-error.handler");
const authorization_handler_1 = require("middleware/authorization.handler");
const config_1 = require("config");
const cors_1 = __importDefault(require("cors"));
class App {
    constructor(port) {
        this.app = (0, express_1.default)();
        this.port = port;
        this.setMiddleWare();
    }
    async initServer() {
        await new Promise((resolve) => {
            this.createDatabaseConnection().then(() => {
                (0, routing_controllers_1.useContainer)(typedi_1.default);
                (0, routing_controllers_1.useExpressServer)(this.app, {
                    controllers: [(0, path_1.join)(__dirname + `/api/**/*.controller.${config_1.IS_DEV ? 'ts' : 'js'}`)],
                    middlewares: [global_error_handler_1.GlobalErrorHandler],
                    authorizationChecker: authorization_handler_1.AuthorizationHandler,
                    defaultErrorHandler: false,
                });
                this.server = this.app.listen(this.port, () => {
                    console.log('Service Start');
                    resolve(true);
                });
            });
        });
    }
    setMiddleWare() {
        this.app.use((0, morgan_1.default)('dev'));
        this.app.use(body_parser_1.default.json());
        this.app.use(body_parser_1.default.urlencoded({ extended: true }));
        this.app.use((0, cors_1.default)());
    }
    async createDatabaseConnection() {
        const connectionOpts = {
            type: 'sqlite',
            database: 'test.db',
            entities: [(0, path_1.join)(__dirname + `/api/**/*.entity.${config_1.IS_DEV ? 'ts' : 'js'}`)],
            synchronize: true,
        };
        (0, typeorm_1.useContainer)(typedi_1.default);
        await (0, typeorm_1.createConnection)(connectionOpts);
    }
}
exports.App = App;
//# sourceMappingURL=app.js.map