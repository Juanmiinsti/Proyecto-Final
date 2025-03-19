extends GridContainer


@export var character_slot:PackedScene
@export var characters:Array[Character]

func _ready():
	load_characters()

func load_characters():
	for character in characters:
		var slot=character_slot.instantiate()
		slot.icon=character.icon
		slot.pressed
		add_child(slot)

# Called every frame. 'delta' is the elapsed time since the previous frame.
func _on_pressed(character:Character):
	pass
