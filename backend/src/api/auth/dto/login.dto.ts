import { IsNotEmpty } from 'class-validator';

export class LoginDTO {
  @IsNotEmpty()
  public id: string;

  @IsNotEmpty()
  public name: string;
}
