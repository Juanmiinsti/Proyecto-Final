class_name Hurtbox
extends Area2D

func _init() -> void:
	collision_layer = 0
	collision_mask = 2

func _ready() -> void:
	area_entered.connect(_on_area_entered)

func _on_area_entered(hitbox1: Hitbox) -> void:
	if hitbox1 == null:
		return
	if owner.has_method("take_damage"):
		owner.take_damage(hitbox1.getDamage())
