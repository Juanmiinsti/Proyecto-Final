extends GridContainer


@export var character_slot:PackedScene
@export var characters:Array[Character]
@export var numplayer:int

func _ready():
	load_characters()

func load_characters():
	for character in characters:
		var slot=character_slot.instantiate()
		slot.icon=character.icon
		slot.pressed.connect(_on_pressed.bind(character))
		add_child(slot)

# Called every frame. 'delta' is the elapsed time since the previous frame.
func _on_pressed(character:Character):
	if numplayer==1:
		Persistence.character=character
		Persistence.charname1=character.name
	else:
		Persistence.character2=character
		Persistence.charname2=character.name
