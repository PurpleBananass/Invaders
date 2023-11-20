import { Crud } from './entity/rank.entity';
import { EntityRepository, Repository } from 'typeorm';

@EntityRepository(Crud)
export class CrudRepository extends Repository<Crud> {}
