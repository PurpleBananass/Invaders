import { BaseException } from 'exceptions/base.exception';
import { Service } from 'typedi';
import { InjectRepository } from 'typeorm-typedi-extensions';
import { AuthRepository } from './auth.repository';
import { LoginDTO } from './dto/login.dto';

@Service()
export class AuthService {
  constructor(
    @InjectRepository()
    private readonly authRepo: AuthRepository,
  ) {}

  public async login(loginDTO: LoginDTO) {
    try {
      let userInfo = await this.authRepo.findOne(loginDTO);

      if (!userInfo) {
        userInfo = await this.authRepo.save(loginDTO);
      }

      return userInfo;
    } catch (e) {
      throw new BaseException(400, '로그인 중 알 수 없는 에러 발생', e);
    }
  }
}
