import { exec, ExecOptions } from 'shelljs';

export interface IExecFunctionOptions extends ExecOptions {
  silent?: boolean;
  async?: false;
}

export function execAsync(command: string, options: IExecFunctionOptions = {}): Promise<string> {
  return new Promise((resolve, reject) => {
    exec(command, { ...options, async: false }, (code: number, stdout: string, stderr: string) => {
      if (code !== 0) {
        const e: Error = new Error();
        e.message = stderr;
        e.name = String(code);
        reject(e);
      } else {
        resolve(stdout);
      }
    });
  });
}
