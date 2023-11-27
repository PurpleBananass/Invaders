import appModulePath from 'app-module-path';
import 'reflect-metadata';

appModulePath.addPath(__dirname);

import { App } from './app';

(async () => {
  const app = new App(3200);
  await app.initServer();
})();
