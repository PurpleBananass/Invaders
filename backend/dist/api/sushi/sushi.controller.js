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
exports.SushiController = void 0;
const routing_controllers_1 = require("routing-controllers");
const create_sushi_dto_1 = require("./dto/create-sushi.dto");
const sushi_service_1 = require("./sushi.service");
let SushiController = exports.SushiController = class SushiController {
    constructor(sushiService) {
        this.sushiService = sushiService;
    }
    async get() {
        return this.sushiService.getAllSushi();
    }
    async getOne(id) {
        return this.sushiService.getSushiById(id);
    }
    async create(body) {
        return this.sushiService.create(body);
    }
    async deleteOne(id) {
        return this.sushiService.deleteSushiById(id);
    }
};
__decorate([
    (0, routing_controllers_1.Get)(),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", []),
    __metadata("design:returntype", Promise)
], SushiController.prototype, "get", null);
__decorate([
    (0, routing_controllers_1.Get)('/:id'),
    __param(0, (0, routing_controllers_1.Param)('id')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Number]),
    __metadata("design:returntype", Promise)
], SushiController.prototype, "getOne", null);
__decorate([
    (0, routing_controllers_1.Post)(),
    __param(0, (0, routing_controllers_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [create_sushi_dto_1.CreateSushiDTO]),
    __metadata("design:returntype", Promise)
], SushiController.prototype, "create", null);
__decorate([
    (0, routing_controllers_1.Delete)('/:id'),
    __param(0, (0, routing_controllers_1.Param)('id')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Number]),
    __metadata("design:returntype", Promise)
], SushiController.prototype, "deleteOne", null);
exports.SushiController = SushiController = __decorate([
    (0, routing_controllers_1.JsonController)('/sushi'),
    __metadata("design:paramtypes", [sushi_service_1.SushiService])
], SushiController);
//# sourceMappingURL=sushi.controller.js.map