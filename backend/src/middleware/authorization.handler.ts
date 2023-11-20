import { BaseException } from 'exceptions/base.exception';
import { verify } from 'jsonwebtoken';
import { Action } from 'routing-controllers';

export const AuthorizationHandler = async (action: Action) => {
  try {
    const token = action.request.headers['authorization'];

    verify(token, 'tmp');

    return true;
  } catch (e) {
    throw new BaseException(400, 'Bad Token', e);
  }
};
