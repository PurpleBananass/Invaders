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

  public async getByMode(mode: number) {
    try {
      return await this.repo.find({mode});
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

  public async createRank(createDTO: CreateDTO) {
    try {
      const currentRanks = (await this.repo.find({ mode: createDTO.mode })).sort((a, b) => b.score - a.score);

      // check if the score is higher than the lowest score (ranks.length = 10)
      if (currentRanks.length === 10 && currentRanks[9].score > createDTO.score) {
        return currentRanks;
      } else {
        // if the score is higher than the lowest score, delete the lowest score
        if (currentRanks.length === 10) {
          await this.repo.delete(currentRanks[9].id);
        }
        // insert the new score
        await this.repo.save(createDTO);
      }

      return (await this.repo.find()).sort((a, b) => b.score - a.score);
    } catch (e) {
      throw new BaseException(400, 'create error', e);
    }
  }
}
