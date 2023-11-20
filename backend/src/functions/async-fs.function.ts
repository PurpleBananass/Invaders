import { readFile } from 'fs';

export const readFileAsync = async (path: string): Promise<Buffer> => {
  return new Promise((res, rej) => {
    readFile(path, (err, data) => {
      if (err) {
        rej(err);
      }

      res(data);
    });
  });
};
