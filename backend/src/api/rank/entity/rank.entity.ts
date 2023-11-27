/* eslint-disable indent */
import { Column, Entity, PrimaryColumn } from 'typeorm';

@Entity('rank')
export class Rank {
  @PrimaryColumn({ type: 'varchar', length: 10 })
  id: string;

  @Column({ type: 'int' })
  score: number;

  @Column({ type: 'int' })
  rank: number;
}
