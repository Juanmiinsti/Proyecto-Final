extends CharacterBody2D
class_name pCharacterOnline
# Exportamos datos para configurar desde el servidor
@export var character: Character
@export var HealthBar: Healthbar
@export var numPlayer: int
@export var damage: int

const SPEED = 300.0
const JUMP_VELOCITY = -400.0

@onready var hitbox = $Hitbox
var currentHealth = 0

# Controles asignados según jugador
var controlsP1 = ["up1player", "pegar", "ui_left", "ui_right"]
var controlsP2 = ["up2player", "hitTwoplayer", "left2player", "right2player"]
var controls: Array
@export var player :=1:
	set(id):
		player = id
		$PlayerInput.set_multiplayer_authority(id)
		


var is_attacking = false

func _ready():
	# Asigna controles según el número de jugador
	if numPlayer == 1:
		controls = controlsP1
	else:
		controls = controlsP2
	character = Persistence.character
	# La salud inicial se setea aquí para estar seguro de que character ya fue asignado
	currentHealth = 200

func _physics_process(delta: float) -> void:
	# Solo el dueño del nodo puede controlar
	if not is_multiplayer_authority():
		return

	if not is_on_floor():
		velocity += get_gravity() * delta

	# Saltar
	if Input.is_action_just_pressed(controls[0]) and is_on_floor():
		velocity.y = JUMP_VELOCITY
		rpc("play_jump_anim")

	# Atacar
	if Input.is_action_just_pressed(controls[1]) and not is_attacking:
		attack()

	# Movimiento horizontal
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

func attack():
	is_attacking = true
	hitbox.get_node("CollisionShape2D").disabled = false
	rpc("play_attack_anim")
	await $AnimationPlayer.animation_finished
	hitbox.get_node("CollisionShape2D").disabled = true
	is_attacking = false

# Función para recibir daño
func take_damage(amount: int):
	currentHealth -= amount
	HealthBar.update_health_bar(currentHealth)

# RPCs para animaciones sincronizadas
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
