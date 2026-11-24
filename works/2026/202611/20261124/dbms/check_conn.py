"""MS-SQL 접속 확인 스크립트.

접속 정보는 같은 폴더의 note.md 에서 읽는다 (password 가 바뀌면 note.md 만 수정).
환경변수 MSSQL_PASSWORD 가 있으면 note.md 의 password 보다 우선한다.

사용: python check_conn.py
필요: pip install pymssql
"""
import os
import re
import sys
from pathlib import Path

import pymssql

NOTE = Path(__file__).with_name("note.md")


def load_config(path: Path) -> dict:
    """note.md 의 'key : value' 줄을 파싱한다."""
    cfg = {}
    for line in path.read_text(encoding="utf-8").splitlines():
        m = re.match(r"\s*([A-Za-z]+)\s*:\s*(.*?)\s*$", line)
        if m:
            cfg[m.group(1).lower()] = m.group(2)
    return cfg


def main() -> int:
    cfg = load_config(NOTE)
    password = os.environ.get("MSSQL_PASSWORD") or cfg["password"]
    print(f"접속 시도: {cfg['ip']}:{cfg['port']} / DB={cfg['db']} / user={cfg['user']}")
    try:
        conn = pymssql.connect(
            server=cfg["ip"],
            port=int(cfg["port"]),
            user=cfg["user"],
            password=password,
            database=cfg["db"],
            login_timeout=10,
            timeout=10,
        )
    except pymssql.Error as e:
        code, msg = (e.args + (None, None))[:2] if len(e.args) > 1 else (None, e.args[0])
        if isinstance(e.args[0], tuple):  # (code, msg) 가 한 겹 더 싸인 경우
            code, msg = e.args[0][:2]
        if isinstance(msg, bytes):
            msg = msg.decode("utf-8", errors="replace")
        print(f"[실패] {type(e).__name__} (code {code}): {str(msg).splitlines()[0]}")
        return 1

    with conn:
        cur = conn.cursor()
        cur.execute("SELECT @@VERSION, DB_NAME(), SUSER_SNAME()")
        version, db, login = cur.fetchone()
        print("[성공] 접속 확인 완료")
        print(f"  DB    : {db}")
        print(f"  login : {login}")
        print(f"  버전  : {version.splitlines()[0]}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
