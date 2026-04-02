import click
import requests
import json

API_BASE = "http://localhost:8080/api"

@click.group()
def cli():
    """智能巡视 CLI 工具"""
    pass

@cli.command()
@click.option('--username', prompt=True)
@click.option('--password', prompt=True, hide_input=True)
def login(username, password):
    """登录系统"""
    resp = requests.post(f"{API_BASE}/auth/login", json={
        "username": username,
        "password": password
    })
    if resp.status_code == 200:
        data = resp.json()
        if data.get("code") == 200:
            click.echo("✅ 登录成功")
        else:
            click.echo(f"❌ 登录失败：{data.get('message')}")
    else:
        click.echo("❌ 网络错误")

@cli.command()
def inspections():
    """查看巡视任务列表"""
    resp = requests.get(f"{API_BASE}/inspection/tasks")
    if resp.status_code == 200:
        data = resp.json()
        click.echo(json.dumps(data, indent=2, ensure_ascii=False))

@cli.command()
def defects():
    """查看缺陷记录列表"""
    resp = requests.get(f"{API_BASE}/defects")
    if resp.status_code == 200:
        data = resp.json()
        click.echo(json.dumps(data, indent=2, ensure_ascii=False))

if __name__ == "__main__":
    cli()
