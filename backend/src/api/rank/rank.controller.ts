import { Get, Post, JsonController, Body } from 'routing-controllers';
import { RankService } from './rank.service';
import { CreateDTO } from './dto/create.dto';

@JsonController('/rank')
export class RankController {
  constructor(private readonly crudService: RankService) {}

  @Get()
  public async get() {
    return this.crudService.getAll();
  }

  @Get('/1p')
  public async getPlayer1() {
    return this.crudService.getByMode(1);
  }

  @Get('/2p')
  public async getPlayer2() {
    return this.crudService.getByMode(2);
  }

  @Post()
  public async postPlayer1(@Body() body: CreateDTO) {
    return this.crudService.createRank(body);
  }
}
