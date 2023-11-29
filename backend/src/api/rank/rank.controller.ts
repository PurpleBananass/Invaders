import { Get, JsonController } from 'routing-controllers';
import { RankService } from './rank.service';

@JsonController('/rank')
export class RankController {
  constructor(private readonly crudService: RankService) {}

  @Get()
  public async get() {
    return this.crudService.getAll();
  }
}
