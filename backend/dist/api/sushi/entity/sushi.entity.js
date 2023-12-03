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
exports.Sushi = void 0;
const typeorm_1 = require("typeorm");
let Sushi = exports.Sushi = class Sushi {
};
__decorate([
    (0, typeorm_1.PrimaryGeneratedColumn)({ type: 'int', name: 'id' }),
    __metadata("design:type", Number)
], Sushi.prototype, "id", void 0);
__decorate([
    (0, typeorm_1.Column)('varchar', { name: 'password' }),
    __metadata("design:type", String)
], Sushi.prototype, "name", void 0);
__decorate([
    (0, typeorm_1.Column)('varchar', { name: 'location' }),
    __metadata("design:type", String)
], Sushi.prototype, "location", void 0);
__decorate([
    (0, typeorm_1.Column)('varchar', { name: 'phone' }),
    __metadata("design:type", String)
], Sushi.prototype, "phone", void 0);
__decorate([
    (0, typeorm_1.Column)('datetime', {
        name: 'created_at',
        default: () => 'CURRENT_TIMESTAMP',
    }),
    __metadata("design:type", Date)
], Sushi.prototype, "createdAt", void 0);
__decorate([
    (0, typeorm_1.Column)('datetime', {
        name: 'updated_at',
        default: () => 'CURRENT_TIMESTAMP',
    }),
    __metadata("design:type", Date)
], Sushi.prototype, "updatedAt", void 0);
exports.Sushi = Sushi = __decorate([
    (0, typeorm_1.Entity)('sushi')
], Sushi);
//# sourceMappingURL=sushi.entity.js.map