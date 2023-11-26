import { Entity, PrimaryColumn, Column, BaseEntity } from 'typeorm';

@Entity('users')
export class User extends BaseEntity {
  @PrimaryColumn({ type: 'varchar', length: 10 })
  id: string;

  @Column({ type: 'varchar', length: 5 })
  name: string;

  @Column({ type: 'int' })
  score: number;
}
