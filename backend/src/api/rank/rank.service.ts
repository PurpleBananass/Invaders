import { BaseException } from 'exceptions/base.exception';
import { Service } from 'typedi';
import { InjectRepository } from 'typeorm-typedi-extensions';
import { CrudRepository } from './rank.repository';
import { CreateDTO } from './dto/create.dto';

@Service()
export class CrudService {
  constructor(
    @InjectRepository()
    private readonly crudRepo: CrudRepository,
  ) {}

  public async getAll() {
    try {
      return await this.crudRepo.find();
    } catch (e) {
      throw new BaseException(400, 'get list error', e);
    }
  }

  public async getById(id: number) {
    try {
      return await this.crudRepo.findOne(id);
    } catch (e) {
      throw new BaseException(400, 'get by id error', e);
    }
  }

  public async deleteById(id: number) {
    try {
      return await this.crudRepo.delete(id);
    } catch (e) {
      throw new BaseException(400, 'delete error', e);
    }
  }

  public async create(createDTO: CreateDTO) {
    try {
      await this.crudRepo.save(createDTO);
    } catch (e) {
      throw new BaseException(400, 'create error', e);
    }
  }
}
