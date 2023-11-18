import { IsNotEmpty } from 'class-validator';

export class SingupDTO {
  @IsNotEmpty()
  public username: string;

  @IsNotEmpty()
  public password: string;
}
