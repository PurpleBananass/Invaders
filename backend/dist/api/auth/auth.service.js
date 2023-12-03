"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.AuthService = void 0;
const base_exception_1 = require("exceptions/base.exception");
const typedi_1 = require("typedi");
const typeorm_typedi_extensions_1 = require("typeorm-typedi-extensions");
const auth_repository_1 = require("./auth.repository");
let AuthService = class AuthService {
    constructor(authRepo) {
        this.authRepo = authRepo;
    }
    async login(loginDTO) {
        try {
            let userInfo = await this.authRepo.findOne(loginDTO);
            if (!userInfo) {
                userInfo = await this.authRepo.save(loginDTO);
            }
            return userInfo;
        }
        catch (e) {
            throw new base_exception_1.BaseException(400, '로그인 중 알 수 없는 에러 발생', e);
        }
    }
};
AuthService = __decorate([
    (0, typedi_1.Service)(),
    __param(0, (0, typeorm_typedi_extensions_1.InjectRepository)()),
    __metadata("design:paramtypes", [auth_repository_1.AuthRepository])
], AuthService);
exports.AuthService = AuthService;
//# sourceMappingURL=auth.service.js.map