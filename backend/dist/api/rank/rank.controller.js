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
Object.defineProperty(exports, "__esModule", { value: true });
exports.RankController = void 0;
const routing_controllers_1 = require("routing-controllers");
const rank_service_1 = require("./rank.service");
let RankController = class RankController {
    constructor(crudService) {
        this.crudService = crudService;
    }
    async get() {
        return this.crudService.getAll();
    }
};
__decorate([
    (0, routing_controllers_1.Get)(),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", []),
    __metadata("design:returntype", Promise)
], RankController.prototype, "get", null);
RankController = __decorate([
    (0, routing_controllers_1.JsonController)('/rank'),
    __metadata("design:paramtypes", [rank_service_1.RankService])
], RankController);
exports.RankController = RankController;
//# sourceMappingURL=rank.controller.js.map