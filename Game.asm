title Pacman Game
.model small
.data
col db 20
row db 20
wrow db 0
wcol db 0
w1Col db 11, 11, 11, 12, 12, 12, 13, 13, 13
w1Row db 2, 3, 4, 2, 3, 4, 2, 3, 4
scoreArr db 0, 0, 0, 0
;scoreArr db 4 dup(?)
scoreMess db "Score: $"
hscoreMess db "High Score: $"
lifeMess db "<3: $"
score db 0
.stack 100h
.code
sidebar proc

	mov dl, 26
	mov dh, 1
	xor bh, bh
	mov ah, 02h
	int 10h
	
	mov dx, offset scoreMess
	mov ah, 09h
	int 21h
	
	mov si, 0
	scoreLoop:
		mov dl, scoreArr[si]
		add dl, 30h
		mov ah, 02h
		int 21h
		inc si
		cmp si, 4
	jl scoreLoop
	
	mov dl, 26
	mov dh, 2
	xor bh, bh
	mov ah, 02h
	int 10h
	
	mov dx, offset lifeMess	
	mov ah, 09h
	int 21h
	
	mov dl, 26
	mov dh, 20
	xor bh, bh
	mov ah, 02h
	int 10h
	
	mov dx, offset hscoreMess
	mov ah, 09h
	int 21h
	
ret
sidebar endp

printwall proc
	xor bh, bh
	mov ah, 02h
	int 10h
	mov ah, 0Ah
	mov al, 178
	mov bl, 09h
	mov bh, 0
	xor cx, cx
	mov cx, 1
	mov ah, 09h
	int 10h
ret
printwall endp

clr proc
		mov ax, 0600h
		mov bh, 07h
		xor cx, cx
		mov dx, 184fh
		int 10h
		ret
clr endp
outline proc
	mov si, 0
	outlloop1:
		mov dl, 0
		mov dh, wrow
		call printwall
		mov dl, 1
		mov dh, wrow
		call printwall
		
		mov dl, 24
		mov dh, wrow
		call printwall
		mov dl, 23
		mov dh, wrow
		call printwall
		
		inc wrow
		inc si
		cmp si, 24
	jl outlloop1
	
	mov si, 0
	outlloop2:
		mov dl, wcol
		mov dh, 0
		call printwall
		mov dl, wcol
		mov dh, 1
		call printwall
		
		mov dl, wcol
		mov dh, 24
		call printwall
		mov dl, wcol
		mov dh, 23
		call printwall
		
		inc wcol
		inc si
		cmp si, 25
	jl outlloop2
	mov wrow, 0
	mov wcol, 0
	ret
outline endp

wall1 proc
	mov si, 0
	wall1loop:
		mov dl, w1Col[si]
		mov dh, w1Row[si]
		call printwall
		inc si
		cmp si, 9
	jl wall1loop
	ret
wall1 endp
; 0 1 2 3
scored proc
	inc score
	mov si, 3
	cmp scoreArr[si], 9
	jge inc1
	
	inc scoreArr[si]
	jmp ex

inc1:
	mov si, 2
	cmp scoreArr[si], 9
	jge inc2
	
	inc scoreArr[si]
	mov si, 3
	mov scoreArr[si], 0
	jmp ex

inc2:
	mov si, 1
	cmp scoreArr[si], 9
	jge inc3
	
	inc scoreArr[si]
	mov si, 2
	mov scoreArr[si], 0
	jmp ex
	
	inc scoreArr[si]
	mov si, 2
	mov scoreArr[si], 0
	mov si, 3
	mov scoreArr[si], 0
	jmp ex

inc3:
	mov si, 0
	inc scoreArr[si]
	mov si, 1
	mov scoreArr[si], 0
	mov si, 2
	mov scoreArr[si], 0
	mov si, 3
	mov scoreArr[si], 0	
ex:
	
ret
scored endp

    main proc
		
		mov ax, @data
		mov ds, ax
		
		mov al, 01h
		mov ah, 00h
		int 10h
		
		mov cx, 3200h
		mov ah, 01h
		int 10h
		call sidebar
		call outline
		call wall1
		
		
		
	scan:
						
			mov ah, 01h
			int 21h
			
			cmp al, 1Bh
			je exit	
			
			cmp al, 75 
			je left
			cmp al, 'a'
			je left
			
			cmp al, 77
			je right
			cmp al, 'd'
			je right
			
			cmp al, 72 
			je up
			cmp al, 'w'
			je up
			
			cmp al, 80
			je down				
			cmp al, 's'
			je down				
			
		jmp scan
					
		exit:
			call clr
			mov ax, 4c00h
			int 21h	

		left:
			call clr
			call sidebar
			call outline
			call wall1
			
			cmp col, 2
			je l
			
			mov bp, 0
			loop1:
				mov dh, w1Col[bp]
				inc dh
				cmp col, dh
				je blockLeft
				inc bp
				cmp bp, 9
			jle loop1
	
		cont1:
			dec col		
			mov dl, col
			mov dh, row
			xor bh, bh
			mov ah, 02h
			int 10h
			
			mov ah, 0Ah
			mov al, '>'
			mov bl, 0Eh
			mov bh, 0
			xor cx, cx
			mov cx, 1
			mov ah, 09h
			int 10h
			jmp scan
		
		right:
			call clr
			call sidebar
			call outline
			call wall1
			
			cmp col, 22
			je r

		cont2:
			inc col
			mov dl, col
			mov dh, row
			xor bh, bh
			mov ah, 02h
			int 10h
			
			mov ah, 0Ah
			mov al, '<'
			mov bl, 0Eh
			mov bh, 0
			xor cx, cx
			mov cx, 1
			mov ah, 09h
			int 10h
			jmp scan
		
		up:
			call clr
			call sidebar
			call outline			
			call wall1
				
			cmp row, 2
			je u

		cont3:	
			dec row
			mov dl, col
			mov dh, row
			xor bh, bh
			mov ah, 02h
			int 10h
			
			mov ah, 0Ah
			mov al, 'v'
			mov bl, 0Eh
			mov bh, 0
			xor cx, cx
			mov cx, 1
			mov ah, 09h
			int 10h
			jmp scan
			
		down:
			call clr
			call sidebar
			call outline			
			call wall1
			
			cmp row, 22
			je d

		cont4:
			inc row
			mov dl, col
			mov dh, row
			xor bh, bh
			mov ah, 02h
			int 10h
			
			mov ah, 0Ah
			mov al, '^'
			mov bl, 0Eh
			mov bh, 0
			xor cx, cx
			mov cx, 1
			mov ah, 09h
			int 10h
			jmp scan
		
		l:
			inc col
			mov dl, col
			mov dh, row
			xor bh, bh
			mov ah, 02h
			int 10h
			jmp left
	
		r:
			dec col
			mov dl, col
			mov dh, row
			xor bh, bh
			mov ah, 02h
			int 10h
			jmp right
		
		u:
			inc row
			mov dl, col
			mov dh, row
			xor bh, bh
			mov ah, 02h
			int 10h
			jmp up

		
		d:
			dec row
			mov dl, col
			mov dh, row
			xor bh, bh
			mov ah, 02h
			int 10h
			jmp down
			
		blockLeft:
			mov dh, porR[bp]
			cmp row, dh
			je l
			jmp cont1
	
	main    endp
    end     main
