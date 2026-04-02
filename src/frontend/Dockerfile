FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
# 配置 npm 淘宝镜像源加速下载
RUN npm config set registry https://registry.npmmirror.com && npm install
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
