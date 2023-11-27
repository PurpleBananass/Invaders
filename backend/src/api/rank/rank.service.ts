import { BaseException } from 'exceptions/base.exception';
import { Service } from 'typedi';
import { InjectRepository } from 'typeorm-typedi-extensions';
import { RankRepository } from './rank.repository';
import { CreateDTO } from './dto/create.dto';

@Service()
export class RankService {
  constructor(
    @InjectRepository()
    private readonly repo: RankRepository,
  ) {}

  public async getAll() {
    try {
      return await this.repo.find();
    } catch (e) {
      throw new BaseException(400, 'get list error', e);
    }
  }

  public async getById(id: number) {
    try {
      return await this.repo.findOne(id);
    } catch (e) {
      throw new BaseException(400, 'get by id error', e);
    }
  }

  public async deleteById(id: number) {
    try {
      return await this.repo.delete(id);
    } catch (e) {
      throw new BaseException(400, 'delete error', e);
    }
  }

  public async create(createDTO: CreateDTO) {
    try {
      await this.repo.save(createDTO);
    } catch (e) {
      throw new BaseException(400, 'create error', e);
    }
  }
}
