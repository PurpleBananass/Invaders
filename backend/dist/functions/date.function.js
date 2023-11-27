"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.getYYYYMMDD = exports.getToday = void 0;
const getToday = () => {
    const date = new Date();
    const year = date.getFullYear();
    const month = ('0' + (1 + date.getMonth())).slice(-2);
    const day = ('0' + date.getDate()).slice(-2);
    return year + month + day;
};
exports.getToday = getToday;
const getYYYYMMDD = (date) => {
    const year = date.getFullYear();
    const month = ('0' + (1 + date.getMonth())).slice(-2);
    const day = ('0' + date.getDate()).slice(-2);
    return year + month + day;
};
exports.getYYYYMMDD = getYYYYMMDD;
//# sourceMappingURL=date.function.js.map