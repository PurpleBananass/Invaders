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
exports.RankService = void 0;
const base_exception_1 = require("exceptions/base.exception");
const typedi_1 = require("typedi");
const typeorm_typedi_extensions_1 = require("typeorm-typedi-extensions");
const rank_repository_1 = require("./rank.repository");
let RankService = class RankService {
    constructor(repo) {
        this.repo = repo;
    }
    async getAll() {
        try {
            return await this.repo.find();
        }
        catch (e) {
            throw new base_exception_1.BaseException(400, 'get list error', e);
        }
    }
    async getById(id) {
        try {
            return await this.repo.findOne(id);
        }
        catch (e) {
            throw new base_exception_1.BaseException(400, 'get by id error', e);
        }
    }
    async deleteById(id) {
        try {
            return await this.repo.delete(id);
        }
        catch (e) {
            throw new base_exception_1.BaseException(400, 'delete error', e);
        }
    }
    async create(createDTO) {
        try {
            await this.repo.save(createDTO);
        }
        catch (e) {
            throw new base_exception_1.BaseException(400, 'create error', e);
        }
    }
};
RankService = __decorate([
    (0, typedi_1.Service)(),
    __param(0, (0, typeorm_typedi_extensions_1.InjectRepository)()),
    __metadata("design:paramtypes", [rank_repository_1.RankRepository])
], RankService);
exports.RankService = RankService;
//# sourceMappingURL=rank.service.js.map