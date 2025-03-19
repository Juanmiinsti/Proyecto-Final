extends CharacterBody2D
class_name player2
const SPEED = 300.0
const JUMP_VELOCITY = -400.0

@export var character: Character


var maxHealth=10
@onready var currentHealth: int = maxHealth

@onready var p2HealthBar = $"../CanvasLayer/ProgressBar2"

var is_attacking = false  # Nueva variable para controlar el ataque
func _ready():
	p2HealthBar.max_value=maxHealth
	p2HealthBar.value=currentHealth
	
func _physics_process(delta: float) -> void:
	# Aplicar gravedad
	if not is_on_floor():
		velocity += get_gravity() * delta

	# Manejo del salto
	if Input.is_action_just_pressed("up2player") and is_on_floor():
		velocity.y = JUMP_VELOCITY

	# Manejo del ataque
	if Input.is_action_just_pressed("hitTwoplayer") and not is_attacking:
		take_damage(2)
		is_attacking = true
		$AllAnimations.play("Attack_"+character.animation_name)
		await $AllAnimations.animation_finished
		is_attacking = false  # Permitir otras acciones después del ataque
		return  # Evita que se ejecute el resto del código mientras ataca

	# Movimiento solo si no está atacando
	if not is_attacking:
		var direction := Input.get_axis("left2player", "right2player")
		if direction:
			velocity.x = direction * SPEED
			if is_on_floor():
				$AllAnimations.set_flip_h(direction < 0)
				$AllAnimations.play("Run_"+character.animation_name)
		else:
			velocity.x = move_toward(velocity.x, 0, SPEED)
			if is_on_floor():
				$AllAnimations.play("Idle_"+character.animation_name)

	move_and_slide()
	
func take_damage(amount: int):
	currentHealth -= amount
	p2HealthBar.update_health_bar(currentHealth)
