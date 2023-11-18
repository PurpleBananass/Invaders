import { BaseException } from 'exceptions/base.exception';
import { Service } from 'typedi';
import { InjectRepository } from 'typeorm-typedi-extensions';
import { AuthRepository } from './auth.repository';
import { LoginDTO } from './dto/login.dto';
import { SingupDTO } from './dto/sign-up.dto';
import { sign } from 'jsonwebtoken';

@Service()
export class AuthService {
  constructor(
    @InjectRepository()
    private readonly authRepo: AuthRepository,
  ) {}

  public async login(loginDTO: LoginDTO) {
    try {
      const userInfo = await this.authRepo.findOne(loginDTO);
      const token = sign(
        {
          id: userInfo.id,
          username: userInfo.username,
        },
        'tmp',
        { expiresIn: '10s', issuer: 'sraccoon' },
      );
      return { token: token };
    } catch (e) {
      throw new BaseException(400, '토큰 발급 중 알 수 없는 에러 발생', e);
    }
  }

  public async signup(signupDTO: SingupDTO) {
    try {
      await this.authRepo.save(signupDTO);
    } catch (e) {
      throw new BaseException(400, e.message, e);
    }
  }
}
