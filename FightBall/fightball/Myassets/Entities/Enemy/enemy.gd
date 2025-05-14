extends CharacterBody2D

const SPEED = 30.0
@onready var maxHealth = 100
@onready var damage = 10
@onready var currentHealth = maxHealth
@onready var healthBar = $Sprite2D/ProgressBar
@onready var navigation_agent:NavigationAgent2D =$NavigationAgent2D

@export var targetTochase : CharacterBody2D
var player: Node2D

func _ready() -> void:
	healthBar.update_health_bar(currentHealth)
	set_physics_process(false)
	call_deferred("wait_for_physics")
	call_deferred("initialize_navigation")
	
func initialize_navigation():
	if targetTochase:
		navigation_agent.target_position = targetTochase.global_position
	
	

func _physics_process(delta: float) -> void:
	if navigation_agent.is_navigation_finished() and\
				targetTochase.global_position  == navigation_agent.target_position:
		return
		
	navigation_agent.target_position=targetTochase.global_position
	# Asegúrate de aplicar la gravedad correctamente
	velocity=global_position.direction_to(navigation_agent.get_next_path_position()) * SPEED
	
	move_and_slide()

func take_damage(amount: int):
	currentHealth -= amount
	healthBar.update_health_bar(currentHealth)

func _on_progress_bar_value_changed(value: float) -> void:
	if currentHealth <= 0:
		queue_free()

func wait_for_physics():
	await get_tree().physics_frame
	set_physics_process(true)
