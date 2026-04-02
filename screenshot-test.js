const { chromium } = require('playwright');

(async () => {
  console.log('🚀 启动浏览器...');
  const browser = await chromium.launch({ 
    headless: true,
    args: ['--no-sandbox', '--disable-setuid-sandbox']
  });
  
  const context = await browser.newContext({
    viewport: { width: 1920, height: 1080 }
  });
  
  const page = await context.newPage();
  
  // 访问登录页面
  console.log('📍 访问 http://localhost...');
  await page.goto('http://localhost', { waitUntil: 'networkidle', timeout: 30000 });
  
  // 截图登录页面
  console.log('📸 截取登录页面...');
  await page.screenshot({ 
    path: 'D:/code/smart-inspection-digital-employee/login-page.png',
    fullPage: true 
  });
  
  // 尝试登录
  console.log('🔑 执行登录...');
  await page.fill('input[type="text"], input[name="username"], #username', 'admin');
  await page.fill('input[type="password"], input[name="password"], #password', 'admin123');
  await page.click('button[type="submit"], .el-button--primary, button:has-text("登录"), button:has-text("Login")');
  
  // 等待导航
  await page.waitForLoadState('networkidle', { timeout: 10000 });
  await page.waitForTimeout(2000);
  
  // 截图登录后页面
  console.log('📸 截取登录后页面...');
  await page.screenshot({ 
    path: 'D:/code/smart-inspection-digital-employee/logged-in-page.png',
    fullPage: true 
  });
  
  console.log('✅ 截图完成！');
  console.log('登录页面：D:/code/smart-inspection-digital-employee/login-page.png');
  console.log('登录后页面：D:/code/smart-inspection-digital-employee/logged-in-page.png');
  
  await browser.close();
})();
