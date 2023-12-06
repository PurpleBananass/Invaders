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
exports.RankController = void 0;
const routing_controllers_1 = require("routing-controllers");
const rank_service_1 = require("./rank.service");
const create_dto_1 = require("./dto/create.dto");
let RankController = class RankController {
    constructor(crudService) {
        this.crudService = crudService;
    }
    async get() {
        return this.crudService.getAll();
    }
    async getPlayer1() {
        return this.crudService.getByMode(1);
    }
    async getPlayer2() {
        return this.crudService.getByMode(2);
    }
    async postPlayer1(body) {
        return this.crudService.createRank(body);
    }
};
__decorate([
    (0, routing_controllers_1.Get)(),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", []),
    __metadata("design:returntype", Promise)
], RankController.prototype, "get", null);
__decorate([
    (0, routing_controllers_1.Get)('/1p'),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", []),
    __metadata("design:returntype", Promise)
], RankController.prototype, "getPlayer1", null);
__decorate([
    (0, routing_controllers_1.Get)('/2p'),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", []),
    __metadata("design:returntype", Promise)
], RankController.prototype, "getPlayer2", null);
__decorate([
    (0, routing_controllers_1.Post)(),
    __param(0, (0, routing_controllers_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [create_dto_1.CreateDTO]),
    __metadata("design:returntype", Promise)
], RankController.prototype, "postPlayer1", null);
RankController = __decorate([
    (0, routing_controllers_1.JsonController)('/rank'),
    __metadata("design:paramtypes", [rank_service_1.RankService])
], RankController);
exports.RankController = RankController;
//# sourceMappingURL=rank.controller.js.map