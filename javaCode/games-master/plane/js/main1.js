/*
 * @Author: Marte
 * @Date:   2017-07-05 17:18:30
 * @Last Modified by:   Marte
 * @Last Modified time: 2017-07-06 09:44:02
 */

'use strict';

var game = new Phaser.Game(240, 400, Phaser.auto, 'game1');


var G = document.getElementById('game1');



game.States = {};
game.States.boot = function() {

	this.preload = function() {
		game.load.image('loading', 'assets/preloader.gif'); //加载进度条图片资源
		game.load.spritesheet('explode1', 'assets/com.png', 64, 256, 4);
		game.load.image('background', 'assets/bg.jpg');
		game.load.spritesheet('startbutton', 'assets/startbutton.png', 100, 40, 2);
	};
	this.create = function() {
		game.state.start('preload'); //加载完成后，调用preload场景
	};
}

game.States.preload = function() {
	this.preload = function() {

		var preloadSprite = game.add.tileSprite(0,0,game.width,game.height, 'background'); //创建显示loading进度的sprite
		 preloadSprite.autoScroll(-10,0);
		//当加载资源过多的时候，会出现滚动条加载的效果。
		
		 this.fs = game.add.tileSprite(0,0,64, 256, 'explode1');
		//game.load.setPreloadSprite(preloadSprite); //用setPreloadSprite方法来实现动态进度条的效果
		this.fs.animations.add('fly');
    	this.fs.animations.play('fly', 4, true);
    	this.startbutton = game.add.button(70, 200, 'startbutton', this.onStartClick, this, 1, 0, 1);
	};
	 this.onStartClick = function() {
	    
	  };
	this.create = function() {
	
		game.physics.enable(this.fs , Phaser.Physics.ARCADE);
		 this.fs.body.gravity.y = 1000;
		 game.input.keyboard.addCallbacks(game,function(e){
		 	console.log(e);
		 },function(){
		 	console.log(2);
		 });

		
		//game.state.start('main');
	};
	this.update =function(){
		
	}
}


game.States.main = function() {
  this.create = function() {
    // 背景
    var bg = game.add.tileSprite(0, 0, game.width, game.height, 'background');
    // 版权
    this.copyright = game.add.image(12, game.height - 16, 'copyright');
    // 我的飞机
    this.myplane = game.add.sprite(100, 100, 'myplane');
    this.myplane.animations.add('fly');
    this.myplane.animations.play('fly', 12, true);
    // 开始按钮
    this.startbutton = game.add.button(70, 200, 'startbutton', this.onStartClick, this, 1, 1, 0);
    // 背景音乐
    this.normalback = game.add.audio('normalback', 0.2, true);
    this.normalback.play();
  };
  this.onStartClick = function() {
    game.state.start('start');
    this.normalback.stop();
  };
};
game.state.add('boot', game.States.boot);
game.state.add('preload', game.States.preload);
game.state.add('main', game.States.main);

game.state.start('boot');