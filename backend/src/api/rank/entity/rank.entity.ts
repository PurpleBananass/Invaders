/* eslint-disable indent */
import { Column, Entity, PrimaryGeneratedColumn } from 'typeorm';

@Entity('rank')
export class Rank {
  @PrimaryGeneratedColumn({ type: 'int' })
  id: string;

  @Column({ type: 'varchar', length: 10 })
  username: string;

  @Column({ type: 'int' })
  score: number;

  @Column({ type: 'int' })
  mode: number;
}
