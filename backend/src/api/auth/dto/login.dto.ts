import { IsNotEmpty } from 'class-validator';

export class LoginDTO {
  @IsNotEmpty()
  public username: string;

  @IsNotEmpty()
  public password: string;
}
