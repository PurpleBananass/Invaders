import { config } from 'dotenv';
config({ path: `.env.local` });

export const { NODE_ENV } = process.env;
export const IS_DEV = process.env.NODE_ENV === 'development';
