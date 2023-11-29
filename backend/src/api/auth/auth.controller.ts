import { Authorized, Body, Get, JsonController, Post } from 'routing-controllers';
import { AuthService } from './auth.service';
import { LoginDTO } from './dto/login.dto';

@JsonController('/auth')
export class AuthController {
  constructor(private readonly authService: AuthService) {}

  @Post('/login')
  public async login(@Body() body: LoginDTO) {
    return await this.authService.login(body);
  }

  @Get('/test')
  @Authorized()
  public test() {
    return 'good';
  }
}
