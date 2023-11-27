"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.upload = void 0;
const aws_sdk_1 = require("aws-sdk");
const s3 = new aws_sdk_1.S3();
const upload = async (bucket, taskName, body) => {
    return await s3
        .upload({
        Bucket: bucket,
        Key: taskName,
        Body: body,
    })
        .promise();
};
exports.upload = upload;
//# sourceMappingURL=s3.function.js.map