//============================智宇科技===========================、
/*前进  按下发出 ONA  松开ONF
  后退：按下发出 ONB  松开ONF
  左转：按下发出 ONC  松开ONF
  右转：按下发出 OND  松开ONF
  停止：按下发出 ONE  松开ONF

  蓝牙程序功能是按下对应的按键执行操，松开按键就停止
*/
char getstr;
int Left_motor = 8;   //左电机(IN3) 输出0  前进   输出1 后退
int Left_motor_pwm = 9;   //左电机PWM调速

int Right_motor_pwm = 10;  // 右电机PWM调速
int Right_motor = 11;  // 右电机后退(IN1)  输出0  前进   输出1 后退

void setup()
{
  //初始化电机驱动IO为输出方式
  Serial.begin(9600);
  pinMode(Left_motor, OUTPUT); // PIN 8 8脚无PWM功能
  pinMode(Left_motor_pwm, OUTPUT); // PIN 9 (PWM)
  pinMode(Right_motor_pwm, OUTPUT); // PIN 10 (PWM)
  pinMode(Right_motor, OUTPUT); // PIN 11 (PWM)
}

void run(int time)     // 前进
{
  digitalWrite(Right_motor, LOW); // 右电机前进
  digitalWrite(Right_motor_pwm, HIGH); // 右电机前进
  analogWrite(Right_motor_pwm, 255); //PWM比例0~255调速，左右轮差异略增减

  digitalWrite(Left_motor, HIGH); // 左电机前进
  digitalWrite(Left_motor_pwm, HIGH); //左电机PWM
  analogWrite(Left_motor_pwm, 255); //PWM比例0~255调速，左右轮差异略增减
  delay(time);   //执行时间，可以调整
}

void brake(int time)         //刹车，停车
{
  digitalWrite(Right_motor_pwm, LOW); // 右电机PWM 调速输出0
  analogWrite(Right_motor_pwm, 0); //PWM比例0~255调速，左右轮差异略增减

  digitalWrite(Left_motor_pwm, LOW); //左电机PWM 调速输出0
  analogWrite(Left_motor_pwm, 0); //PWM比例0~255调速，左右轮差异略增减
  delay(time);//执行时间，可以调整
}

void left(int time)         //左转(左轮不动，右轮前进)
{
  digitalWrite(Right_motor, LOW); // 右电机前进
  digitalWrite(Right_motor_pwm, HIGH); // 右电机前进
  analogWrite(Right_motor_pwm, 255); //PWM比例0~255调速，左右轮差异略增减

  digitalWrite(Left_motor, LOW); // 左电机前进
  digitalWrite(Left_motor_pwm, LOW); //左电机PWM
  analogWrite(Left_motor_pwm, 0); //PWM比例0~255调速，左右轮差异略增减
  delay(time);	//执行时间，可以调整
}

void spin_left(int time)         //左转(左轮后退，右轮前进)
{
  digitalWrite(Right_motor, LOW); // 右电机前进
  digitalWrite(Right_motor_pwm, HIGH); // 右电机前进
  analogWrite(Right_motor_pwm, 255); //PWM比例0~255调速，左右轮差异略增减

  digitalWrite(Left_motor, LOW); // 左电机后退
  digitalWrite(Left_motor_pwm, HIGH); //左电机PWM
  analogWrite(Left_motor_pwm, 255); //PWM比例0~255调速，左右轮差异略增减
  delay(time);	//执行时间，可以调整
}

void right(int time)        //右转(右轮不动，左轮前进)
{
  digitalWrite(Right_motor, LOW); // 右电机不转
  digitalWrite(Right_motor_pwm, LOW); // 右电机PWM输出0
  analogWrite(Right_motor_pwm, 0); //PWM比例0~255调速，左右轮差异略增减

  digitalWrite(Left_motor, HIGH); // 左电机前进
  digitalWrite(Left_motor_pwm, HIGH); //左电机PWM
  analogWrite(Left_motor_pwm, 255); //PWM比例0~255调速，左右轮差异略增减
  delay(time);	//执行时间，可以调整
}

void spin_right(int time)        //右转(右轮后退，左轮前进)
{
  digitalWrite(Right_motor, HIGH); // 右电机后退
  digitalWrite(Right_motor_pwm, HIGH); // 右电机PWM输出1
  analogWrite(Right_motor_pwm, 255); //PWM比例0~255调速，左右轮差异略增减

  digitalWrite(Left_motor, HIGH); // 左电机前进
  digitalWrite(Left_motor_pwm, HIGH); //左电机PWM
  analogWrite(Left_motor_pwm, 255); //PWM比例0~255调速，左右轮差异略增减
  delay(time);	//执行时间，可以调整
}

void back(int time)          //后退
{
  digitalWrite(Right_motor, HIGH); // 右电机后退
  digitalWrite(Right_motor_pwm, HIGH); // 右电机前进
  analogWrite(Right_motor_pwm, 255); //PWM比例0~255调速，左右轮差异略增减

  digitalWrite(Left_motor, LOW); // 左电机后退
  digitalWrite(Left_motor_pwm, HIGH); //左电机PWM
  analogWrite(Left_motor_pwm, 255); //PWM比例0~255调速，左右轮差异略增减
  delay(time);   //执行时间，可以调整
}

void loop()
{
  getstr = Serial.read();
  if (getstr == 'A')
  {
    Serial.println("go forward!");
    run(10);
  }
  else if (getstr == 'B') {
    Serial.println("go back!");
    back(10);
  }
  else if (getstr == 'C') {
    Serial.println("go left!");
    left(10);
  }
  else if (getstr == 'D') {
    Serial.println("go right!");
    right(10);
  }
  else if (getstr == 'F') {
    Serial.println("Stop!");
    brake(10);
  }
  else if (getstr == 'E') {
    Serial.println("Stop!");
    brake(10);
  }

}


