import { Body, Delete, Get, JsonController, Param, Post } from 'routing-controllers';
import { CrudService } from './rank.service';
import { CreateDTO } from './dto/create.dto';

@JsonController('/crud')
export class CrudController {
  constructor(private readonly crudService: CrudService) {}

  @Get()
  public async get() {
    return this.crudService.getAll();
  }

  @Get('/:id')
  public async getOne(@Param('id') id: number) {
    return this.crudService.getById(id);
  }

  @Post()
  public async create(@Body() body: CreateDTO) {
    return this.crudService.create(body);
  }

  @Delete('/:id')
  public async deleteOne(@Param('id') id: number) {
    return this.crudService.deleteById(id);
  }
}
