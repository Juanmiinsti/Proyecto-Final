extends CharacterBody2D


@export var character: Character
@export var HealthBar: Healthbar
@export var numPlayer: int
@export var damage: int

const SPEED = 300.0
const JUMP_VELOCITY = -400.0

@onready var hitbox = $Hitbox
@onready var currentHealth = character.health  # Usa un valor base inicial

# Controles locales
var controlsP1 = ["up1player", "pegar", "ui_left", "ui_right"]
var controlsP2 = ["up2player", "hitTwoplayer", "left2player", "right2player"]
var controls: Array

var is_attacking = false

func _ready():
	if numPlayer == 1:
		character = Persistence.character
		controls = controlsP1
	else:
		character = Persistence.character2
		controls = controlsP2

	set_multiplayer_authority(get_multiplayer_authority())

func _physics_process(delta: float) -> void:
	if not is_multiplayer_authority():
		return  # Solo el dueño puede mover

	if not is_on_floor():
		velocity.y += get_gravity() * delta

	if Input.is_action_just_pressed(controls[0]) and is_on_floor():
		velocity.y = JUMP_VELOCITY
		rpc("play_jump_anim")

	if Input.is_action_just_pressed(controls[1]) and not is_attacking:
		attack()

	var direction := Input.get_axis(controls[2], controls[3])
	if direction:
		velocity.x = direction * SPEED
		if is_on_floor():
			rpc("update_animation", "Run_" + character.animation_name, direction)
	else:
		velocity.x = move_toward(velocity.x, 0, SPEED)
		if is_on_floor():
			rpc("update_animation", "Idle_" + character.animation_name, 0)

	move_and_slide()

# ATAQUE
func attack():
	is_attacking = true
	hitbox.get_node("CollisionShape2D").disabled = false
	rpc("play_attack_anim")
	await $AnimationPlayer.animation_finished
	hitbox.get_node("CollisionShape2D").disabled = true
	is_attacking = false

# DAÑO
func take_damage(amount: int):
	currentHealth -= amount
	HealthBar.update_health_bar(currentHealth)
# RPCs
@rpc("any_peer")
func play_jump_anim():
	$AnimationPlayer.play("Jump_" + character.animation_name)

@rpc("any_peer")
func play_attack_anim():
	$AnimationPlayer.play("Attack_" + character.animation_name)

@rpc("any_peer")
func update_animation(anim: String, dir: float):
	if dir != 0:
		$Sprite2D.scale.x = -1 if dir < 0 else 1
		$Hitbox/CollisionShape2D.position.x = -30 if dir < 0 else 30
	$AnimationPlayer.play(anim)
