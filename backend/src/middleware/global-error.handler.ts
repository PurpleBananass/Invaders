import { Request, Response } from 'express';
import { Middleware, ExpressErrorMiddlewareInterface } from 'routing-controllers';

@Middleware({ type: 'after' })
export class GlobalErrorHandler implements ExpressErrorMiddlewareInterface {
  error(error: any, request: Request, response: Response) {
    console.log(request.headers);
    if (error.message.includes('Invalid body')) {
      response.statusCode = 400;
      response.json({ message: error.message, detail: error.errors.join('\n') });
    } else {
      response.json({ status: error.status, message: error.message });
    }
  }
}
