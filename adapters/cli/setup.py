from setuptools import setup, find_packages

setup(
    name="smart-inspection-cli",
    version="1.0.0",
    packages=find_packages(),
    install_requires=[
        "click>=8.0.0",
        "requests>=2.28.0",
        "pyyaml>=6.0.0",
    ],
    entry_points={
        "console_scripts": [
            "aifei-cli=cli.main:cli",
        ],
    },
)
