import { S3 } from 'aws-sdk';

const s3 = new S3();

export const upload = async (bucket: string, taskName: string, body: Buffer) => {
  return await s3
    .upload({
      Bucket: bucket,
      Key: taskName,
      Body: body,
    })
    .promise();
};
