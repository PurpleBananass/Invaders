import { IsNotEmpty } from 'class-validator';

export class CreateDTO {
  @IsNotEmpty()
  public id: string;

  @IsNotEmpty()
  public score: number;

  @IsNotEmpty()
  public rank: number;
}
