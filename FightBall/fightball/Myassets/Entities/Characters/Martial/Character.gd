extends CharacterBody2D

const SPEED = 300.0
const JUMP_VELOCITY = -400.0

var is_attacking = false  # Nueva variable para controlar el ataque

func _physics_process(delta: float) -> void:
	# Aplicar gravedad
	if not is_on_floor():
		velocity += get_gravity() * delta

	# Manejo del salto
	if Input.is_action_just_pressed("ui_accept") and is_on_floor():
		velocity.y = JUMP_VELOCITY

	# Manejo del ataque
	if Input.is_action_just_pressed("pegar") and not is_attacking:
		is_attacking = true
		$AnimatedSprite2D.play("Attack")
		await $AnimatedSprite2D.animation_finished
		is_attacking = false  # Permitir otras acciones después del ataque
		return  # Evita que se ejecute el resto del código mientras ataca

	# Movimiento solo si no está atacando
	if not is_attacking:
		var direction := Input.get_axis("ui_left", "ui_right")
		if direction:
			velocity.x = direction * SPEED
			if is_on_floor():
				$AnimatedSprite2D.set_flip_h(direction < 0)
				$AnimatedSprite2D.play("Run")
		else:
			velocity.x = move_toward(velocity.x, 0, SPEED)
			if is_on_floor():
				$AnimatedSprite2D.play("Idle")

	move_and_slide()
