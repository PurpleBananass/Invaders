import { IsNotEmpty } from 'class-validator';

export class CreateDTO {
  @IsNotEmpty()
  public username: string;

  @IsNotEmpty()
  public score: number;

  @IsNotEmpty()
  public mode: number;
}
