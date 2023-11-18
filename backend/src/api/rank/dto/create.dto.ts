import { IsNotEmpty } from 'class-validator';

export class CreateDTO {
  @IsNotEmpty()
  public name: string;
}
