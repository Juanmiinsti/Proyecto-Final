extends Node2D

# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	var char=$CharacterBody2D
	char.p1HealthBar=$CanvasLayer/ProgressBar
	char.p1HealthBar.max_value=char.maxHealth
	char.p1HealthBar.value=char.currentHealth


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	pass
