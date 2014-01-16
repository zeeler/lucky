package com.litaook.lottery;

import java.util.Random;

/**
 * 抽奖随机算法
 * @author litao
 *
 */
public class GameMath {
	private Random random;

	public GameMath() {
		this.random = null;
		this.random = new Random();
	}

	public int RandRange(int min, int max) {
		int randomNum = (int) Math.floor(this.random.nextFloat()
				* (max - min + 1))
				+ min;
		return randomNum;
	}

	public int getOrder(int min, int max, int prev) {
		int orderInt = prev;
		if (prev < max)
			orderInt = prev + 1;
		else
			orderInt = min;
		return orderInt;
	}
}