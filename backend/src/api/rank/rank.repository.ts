import { Rank } from './entity/rank.entity';
import { EntityRepository, Repository } from 'typeorm';

@EntityRepository(Rank)
export class RankRepository extends Repository<Rank> {}
