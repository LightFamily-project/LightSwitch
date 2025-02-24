FROM node:20 AS builder
WORKDIR /app

# Install PNPM
RUN corepack enable && corepack prepare pnpm@9.15.4 --activate

# use dependency cache with using package.json& pnpm-lock.yaml
COPY package.json pnpm-lock.yaml ./
RUN pnpm install --frozen-lockfile

COPY . .
RUN pnpm build

# Nginx로 정적 파일 서빙
FROM nginx:1.25

WORKDIR /usr/share/nginx/html
COPY --from=builder /app/.next/static ./static
COPY --from=builder /app/public ./public
COPY --from=builder /app/.next/BUILD_ID .
COPY --from=builder /app/.next/routes-manifest.json .
COPY --from=builder /app/.next/export-marker.json .
COPY --from=builder /app/.next/prerender-manifest.json .

# Nginx 설정 복사
COPY nginx/default.conf /etc/nginx/conf.d/default.conf

# 80 포트에서 실행
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
