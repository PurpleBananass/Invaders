export class BaseException extends Error {
  public status: number;

  constructor(status = 500, message = 'Unknown Error', error: Error) {
    super(message);
    this.status = status;
    this.message = message;

    console.log(error);
  }
}
