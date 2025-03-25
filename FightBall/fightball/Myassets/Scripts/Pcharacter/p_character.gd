extends CharacterBody2D

class_name pCharacter

@export var character: Character
const SPEED = 300.0
const JUMP_VELOCITY = -400.0

@onready var currentHealth
@export var HealthBar :Healthbar
var pegar = "pegar" #que los controles se asignen segun jugador elegido
var controlsP1=["up1player","pegar","ui_left","ui_right"]
var controlsP2=["up2player","hitTwoplayer","left2player","right2player"]
var controls:Array
@export var numPlayer:int
var is_attacking = false  # Nueva variable para controlar el ataque
func _ready():
	if numPlayer==1:
		controls=controlsP1
		character=Persistence.character
	else:
		controls=controlsP2
		character=Persistence.character2
			
	

func _physics_process(delta: float) -> void:
	# Aplicar gravedad
	if not is_on_floor():
		velocity += get_gravity() * delta

	# Manejo del salto
	if Input.is_action_just_pressed(controls[0]) and is_on_floor():
		velocity.y = JUMP_VELOCITY

	# Manejo del ataque
	if Input.is_action_just_pressed(controls[1]) and not is_attacking:
		print(currentHealth)
		take_damage(1)
		print(currentHealth) # Permitir otras acciones después del ataque
		is_attacking = true
		$AnimationPlayer.play("Attack_"+character.animation_name)
		await $AnimationPlayer.animation_finished
		is_attacking = false
		

		return  # Evita que se ejecute el resto del código mientras ataca

	# Movimiento solo si no está atacando
	if not is_attacking:
		var direction := Input.get_axis(controls[2], controls[3])
		if direction:
			velocity.x = direction * SPEED
			if is_on_floor():
				$Sprite2D.scale.x = -1 if direction < 0 else 1  # Voltear personaje
				$AnimationPlayer.play("Run_" + character.animation_name)
		else:
			velocity.x = move_toward(velocity.x, 0, SPEED)
			if is_on_floor():
				$AnimationPlayer.play("Idle_"+character.animation_name)

	move_and_slide()

func take_damage(amount: int):
	currentHealth -= amount
	HealthBar.update_health_bar(currentHealth)
